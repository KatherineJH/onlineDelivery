import React, { useEffect, useState } from "react";
import axios from "axios";
import { Box, Button, TextField, Typography } from "@mui/material";

const Predict = () => {
  const [review, setReview] = useState("");
  const [result, setResult] = useState(null);

  useEffect(() => {
    if (!localStorage.getItem("jwt")) {
      // JWT 토큰이 없으면 Fake JWT 토큰을 저장 (임시 해결책)
      const fakeJwt =
        "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3NDM3NTQwODYsImV4cCI6MTc0NDYxODA4NiwiZW1haWwiOiJ1c2VyM0BlZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9DVVNUT01FUiJdfQ.pcjxL8GD6lDLPggPbBwCDmt_O3QP1ewD1yIkpzsWtl02gAnUjkozcQZicssvipMFdYarlQREnvULpeLJX0ZysA";
      localStorage.setItem("jwt", fakeJwt);
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("jwt");
      if (!token) {
        throw new Error("JWT 토큰이 없습니다. 로그인해주세요.");
      }

      const response = await axios.post(
        "http://localhost:5353/predict",
        {
          review,
          restaurantId: 1, // 테스트용 임시 값 (혹은 상태로 관리해도 됨)
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setResult(response.data);
    } catch (error) {
      console.error("Prediction Error:", error);
      setResult({ prediction: "Error", probability: null });
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        bgcolor: "background.default",
        color: "text.primary",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        flexDirection: "column",
        p: 4,
      }}
    >
      <Typography variant="h4" gutterBottom>
        리뷰 감정 예측
      </Typography>
      <Box
        component="form"
        onSubmit={handleSubmit}
        sx={{ width: "100%", maxWidth: 600 }}
      >
        <TextField
          label="리뷰를 입력하세요"
          multiline
          rows={4}
          fullWidth
          value={review}
          onChange={(e) => setReview(e.target.value)}
          variant="outlined"
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          sx={{ mt: 2 }}
        >
          예측하기
        </Button>
      </Box>

      {result && (
        <Box sx={{ mt: 4 }}>
          <Typography variant="h6">
            예측 결과: <strong>{result.prediction}</strong>
          </Typography>
          {result.confidence !== null && (
            <Typography variant="body1">
              신뢰도: {parseFloat(result.confidence).toFixed(2)}%
            </Typography>
          )}
        </Box>
      )}
    </Box>
  );
};

export default Predict;
