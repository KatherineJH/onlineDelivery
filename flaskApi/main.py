import re
import string
import json
import random
import numpy as np
import pandas as pd
import torch
from flask import Flask, request, jsonify
from your_model_file import ReviewClassifier, ReviewVectorizer  # 필요한 클래스 import

from flask_cors import CORS
app = Flask(__name__)
CORS(app)

# 기본 설정
MODEL_PATH = "review_classifier.pth"
VECTORIZER_PATH = "vectorizer.json"
DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# 벡터라이저 로드
with open(VECTORIZER_PATH) as f:
    vectorizer_contents = json.load(f)

vectorizer = ReviewVectorizer.from_serializable(vectorizer_contents)

# 모델 로드
classifier = ReviewClassifier(num_features=len(vectorizer.review_vocab))
classifier.load_state_dict(torch.load(MODEL_PATH, map_location=DEVICE))
classifier = classifier.to(DEVICE)
classifier.eval()

# 전처리 함수
def preprocess(text):
    text = text.lower()
    text = re.sub(r"([.,!?])", r" \1 ", text)
    text = re.sub(r"[^a-zA-Z.,!?]+", r" ", text)
    return text

# 랜덤 단어 생성
def generate_random_word(length=5):
    return ''.join(random.choices(string.ascii_lowercase, k=length)).capitalize()

# 레스토랑 이름 생성
def generate_restaurant_name():
    pattern = random.choice([
        "맛집 {num}",
        "The {word} House",
        "{word} Bistro",
        "King of {word}",
        "{word} Garden",
        "{word} Palace",
    ])
    if '{num}' in pattern:
        num = random.randint(10000, 99999)
        return pattern.format(num=num)
    elif '{word}' in pattern:
        word = generate_random_word()
        return pattern.format(word=word)

# 기본 라우트
@app.route("/")
def home():
    return "Restaurant Prediction App is running"

# 리뷰 예측 API
@app.route("/predict", methods=["POST"])
def predict():
    data = request.get_json()
    review_text = data.get("review", "")
    
    if not review_text:
        return jsonify({"error": "리뷰가 없습니다."}), 400

    processed = preprocess(review_text)
    vector = vectorizer.vectorize(processed)
    x_data = torch.tensor(vector).unsqueeze(0).to(DEVICE)

    y_pred = classifier(x_data.float())
    prob = torch.sigmoid(y_pred).item()
    label = "Positive" if prob >= 0.5 else "Negative"

    return jsonify({"prediction": label, "probability": prob})

# 상위 긍정 단어 반환 API
@app.route("/top-words", methods=["GET"])
def top_words():
    try:
        fc1_weights = classifier.fc1.weight.detach()[0]  # positive class
        _, indices = torch.sort(fc1_weights, dim=0, descending=True)
        indices = indices.numpy().tolist()

        top_words = [vectorizer.review_vocab.lookup_index(indices[i]) for i in range(20)]
        return jsonify({"top_influential_words": top_words})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# 부정적 예측에 영향을 많이 준 단어 Top 20 API
@app.route("/worst-words", methods=["GET"])
def worst_words():
    try:
        fc1_weights = classifier.fc1.weight.detach()[0]  # positive class
        _, indices = torch.sort(fc1_weights, dim=0, descending=True)
        indices = indices.numpy().tolist()

        # 인덱스를 뒤집어서 하위 20개 선택
        indices.reverse()
        bottom_words = []
        for i in range(20):
            word = vectorizer.review_vocab.lookup_index(indices[i])
            bottom_words.append(word)

        return jsonify({"bottom_influential_words": bottom_words})

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/rank-restaurants", methods=["POST", "OPTIONS"])
def rank_restaurants():
    if request.method == "OPTIONS":
        # CORS preflight 응답 수동 처리
        response = jsonify({'message': 'CORS preflight passed'})
        response.headers.add("Access-Control-Allow-Origin", "*")
        response.headers.add("Access-Control-Allow-Headers", "Content-Type")
        response.headers.add("Access-Control-Allow-Methods", "POST, OPTIONS")
        return response, 200

    try:
        data = request.get_json()
        reviews = data.get("reviews", [])
        ratings = data.get("ratings", [])

        if not reviews or not ratings or len(reviews) != len(ratings):
            return jsonify({"error": "리뷰와 레이블 리스트가 유효하지 않습니다."}), 400

        # 긍정 단어 추출
        fc1_weights = classifier.fc1.weight.detach()[0]
        _, indices = torch.sort(fc1_weights, dim=0, descending=True)
        top_positive_indices = indices[:20].numpy().tolist()
        top_positive_words = [vectorizer.review_vocab.lookup_index(i) for i in top_positive_indices]

        # 점수 계산
        restaurant_scores = {}
        restaurant_names = [generate_restaurant_name() for _ in range(len(reviews))]

        for idx, (review, rating) in enumerate(zip(reviews, ratings)):
            score = 0
            tokens = review.split()
            for word in top_positive_words:
                score += tokens.count(word)
            if rating.lower() == 'positive':
                restaurant_scores[(idx, restaurant_names[idx])] = score

        sorted_restaurants = sorted(restaurant_scores.items(), key=lambda x: x[1], reverse=True)

        result = []
        for rank, ((idx, name), score) in enumerate(sorted_restaurants, 1):
            result.append({
                "rank": rank,
                "restaurant_index": idx,
                "restaurant_name": name,
                "original_review": reviews[idx],
                "original_rating": ratings[idx],
                "score": score
            })

        response = jsonify({"restaurant_ranking": result})
        response.headers.add("Access-Control-Allow-Origin", "*")
        return response

    except Exception as e:
        response = jsonify({"error": str(e)})
        response.headers.add("Access-Control-Allow-Origin", "*")
        return response, 500



# # 레스토랑 점수 및 순위 API
# @app.route("/rank-restaurants", methods=["POST"])
# def rank_restaurants():
#     try:
#         data = request.get_json()
#         reviews = data.get("reviews", [])  # test 리뷰 리스트
#         ratings = data.get("ratings", [])  # 레이블 리스트 ('Positive' 또는 'Negative')

#         if not reviews or not ratings or len(reviews) != len(ratings):
#             return jsonify({"error": "리뷰와 레이블 리스트가 유효하지 않습니다."}), 400

#         # 상위 긍정 단어 가져오기
#         fc1_weights = classifier.fc1.weight.detach()[0]
#         _, indices = torch.sort(fc1_weights, dim=0, descending=True)
#         top_positive_indices = indices[:20].numpy().tolist()
#         top_positive_words = [vectorizer.review_vocab.lookup_index(i) for i in top_positive_indices]

#         # 레스토랑 이름 생성 및 점수 계산
#         restaurant_scores = {}
#         restaurant_names = [generate_restaurant_name() for _ in range(len(reviews))]

#         for idx, (review, rating) in enumerate(zip(reviews, ratings)):
#             score = 0
#             tokens = review.split()
#             for word in top_positive_words:
#                 score += tokens.count(word)
#             if rating.lower() == 'positive':
#                 restaurant_scores[(idx, restaurant_names[idx])] = score

#         # 정렬
#         sorted_restaurants = sorted(restaurant_scores.items(), key=lambda x: x[1], reverse=True)

#         # 결과 생성
#         result = []
#         for rank, ((idx, name), score) in enumerate(sorted_restaurants, 1):
#             result.append({
#                 "rank": rank,
#                 "restaurant_index": idx,
#                 "restaurant_name": name,
#                 "score": score
#             })

#         return jsonify({"restaurant_ranking": result})

#     except Exception as e:
#         return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)


# curl -X POST http://localhost:5000/rank-restaurants -H "Content-Type: application/json" -d "{\"reviews\": [\"The food was amazing and the staff were so friendly!\", \"I hated the meal. It was cold and tasteless.\", \"Loved the spicy noodles and fresh ingredients!\", \"Service was awful, and I won’t come back.\"], \"ratings\": [\"Positive\", \"Negative\", \"Positive\", \"Negative\"]}"
