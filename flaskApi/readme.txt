Yelp Review Polarity Dataset

Version 1, Updated 09/09/2015

ORIGIN

The Yelp reviews dataset consists of reviews from Yelp. It is extracted from the Yelp Dataset Challenge 2015 data. For more information, please refer to http://www.yelp.com/dataset_challenge

The Yelp reviews polarity dataset is constructed by Xiang Zhang (xiang.zhang@nyu.edu) from the above dataset. It is first used as a text classification benchmark in the following paper: Xiang Zhang, Junbo Zhao, Yann LeCun. Character-level Convolutional Networks for Text Classification. Advances in Neural Information Processing Systems 28 (NIPS 2015).


DESCRIPTION

The Yelp reviews polarity dataset is constructed by considering stars 1 and 2 negative, and 3 and 4 positive. For each polarity 280,000 training samples and 19,000 testing samples are take randomly. In total there are 560,000 trainig samples and 38,000 testing samples. Negative polarity is class 1, and positive class 2.

The files train.csv and test.csv contain all the training samples as comma-sparated values. There are 2 columns in them, corresponding to class index (1 and 2) and review text. The review texts are escaped using double quotes ("), and any internal double quote is escaped by 2 double quotes (""). New lines are escaped by a backslash followed with an "n" character, that is "\n".



[vs code terminal에서 안되는 경우]
1. cmd 창 열기

2. 폴더 경로로 이동

3. 가상 환경 만들기
> python -m venv venv

[여기부터는 cmd와 vs code terminal과 동일]
4.  vs code terminal에서 가상 환경으로 들어가기
> venv\Scripts\activate

5. 디렉토리 확인
> dir

6. requirements.txt 에 적힌 것들 설치되는 명령어
> pip install -r requirements.txt
> 만약 경로가 너무 길어서 설치 오류 나는 경우에는 직접 터미널 창에서 하나씩 설치 할 것.

7. 파일 다 작성하고 app 실행
> python main.py

8. pip installed packages 확인
> pip list
> pip show flask-cors
> pip show pandas | findstr Version (버전 확인)
> python -m pip show pandas | findstr Version(버전 확인)

9. 가상 환경 빠져나오기
> deactivate