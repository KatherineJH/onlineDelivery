import { Box, Modal } from "@mui/material";
import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import RegisterForm from "./RegisterForm";
import LoginForm from "./LoginForm";

// 모달 스타일 정의
const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 4,
  border: "2px solid red", // 디버깅용으로 보이게 설정
};

const Auth = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const handleOnClose = () => {
    navigate("/");
  };

  // 디버깅 로그
  console.log("Auth - Current path:", location.pathname);
  console.log("Auth - Modal should open:", 
    location.pathname === "/account/register" || location.pathname === "/account/login"
  );

  return (
    <Modal
      onClose={handleOnClose}
      open={
        location.pathname === "/account/register" ||
        location.pathname === "/account/login"
      }
    >
      <Box sx={style}>
        {console.log("Modal is rendering")}
        {location.pathname === "/account/register" ? <RegisterForm /> : <LoginForm />}
      </Box>
    </Modal>
  );
};

export default Auth;