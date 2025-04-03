import { createTheme } from "@mui/material";

export const DarkTheme = createTheme({
  palette: {
    mode: "dark",
    primary: {
      main: "#FF69B4",
    },
    secondary: {
      main: "#5A20CB",
    },
    black: {
      main: "#2C2C2C", // 밝은 검정
    },
    background: {
      main: "#181818", // 어두운 검정
      default: "#212121", // default background
      paper: "#2A2A2A", // paper background
    },
    text: {
      primary: "#FFFFFF", // white text colour
    },
  },
});
