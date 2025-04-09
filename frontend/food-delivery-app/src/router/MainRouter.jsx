import React from "react";
import { Route, Routes } from "react-router-dom";
import { CustomerRouter } from "./CustomerRouter";
import { AdminRouter } from "./AdminRouter";

export const MainRouter = () => {
  return (
    <Routes>
      <Route path="/*" element={<CustomerRouter />}></Route>
      <Route path="/admin" element={<AdminRouter />}></Route>
    </Routes>
  );
};
