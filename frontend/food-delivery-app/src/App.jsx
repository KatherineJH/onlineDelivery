import { CssBaseline, ThemeProvider } from "@mui/material";
import "./App.css";
import { DarkTheme } from "./theme/DarkTheme";
import { BrowserRouter as Router } from "react-router-dom";
import { MainRouter } from "./router/MainRouter";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { getUser } from "./state/authentication/Action";

function App() {
  const dispatch = useDispatch();
  const { auth } = useSelector((store) => store);
  const jwt = localStorage.getItem("jwt");

  useEffect(() => {
    dispatch(getUser(auth.jwt || jwt));
    // dispatch(findCart(jwt));
  }, [auth.jwt]);

  // useEffect(() => {
  //   dispatch(getRestaurantByUserId(auth.jwt || jwt));
  // }, [auth.user]);

  return (
    <>
      <ThemeProvider theme={DarkTheme}>
        <CssBaseline />
        <MainRouter />
      </ThemeProvider>
    </>
  );
}

export default App;
