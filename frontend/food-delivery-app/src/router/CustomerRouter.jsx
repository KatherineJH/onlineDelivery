import React from "react";
import { Route, Routes } from "react-router-dom";
import { Navbar } from "../customerComponent/navbar/Navbar";
import { Home } from "../customerComponent/home/Home";
import Predict from "../customerComponent/review/Predict";
import TopWords from "../customerComponent/review/Topwords";
import RankedRestaurant from "../customerComponent/review/RankedRestaurant";

export const CustomerRouter = () => {
  return (
    <div>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        {/* 테스트 화면 추가 */}
        <Route path="/predict" element={<Predict />} />
        <Route path="/top-words" element={<TopWords />} />{" "}
        {/* best 20와 worst 20 을 동시에 로드 */}
        <Route path="/rank-restaurants" element={<RankedRestaurant />} />
      </Routes>
    </div>
  );
};
