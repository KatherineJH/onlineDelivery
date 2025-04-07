import React from "react";
import { Route, Routes } from "react-router-dom";
import { Navbar } from "../customerComponent/navbar/Navbar";
import { Home } from "../customerComponent/home/Home";
import Predict from "../customerComponent/review/Predict";
import TopWords from "../customerComponent/review/Topwords";
import RankedRestaurant from "../customerComponent/review/RankedRestaurant";
import Auth from "../customerComponent/auth/Auth";
import Profile from "../customerComponent/profile/Profile";

export const CustomerRouter = () => {
  return (
    <div>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/account/:register" element={<Home />} />
        <Route path="/my-profile/*" element={<Profile />} />
        {/* 테스트 화면 추가 */}
        <Route path="/predict" element={<Predict />} />
        <Route path="/top-words" element={<TopWords />} />{" "}
        {/* best 20와 worst 20 을 동시에 로드 */}
        <Route path="/rank-restaurants" element={<RankedRestaurant />} />
      </Routes>
      <Auth />
    </div>
  );
};
