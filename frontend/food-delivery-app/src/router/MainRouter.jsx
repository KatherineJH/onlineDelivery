import React from "react";
import { Route, Routes } from "react-router-dom";
import { CustomerRouter } from "./CustomerRouter";

export const MainRouter = () => {
  return (
    <Routes>
      <Route path="/*" element={<CustomerRouter />}></Route>
    </Routes>
  );
};
