import React from "react";
import "./Home.css";
// import MultiItemCarousel from "./MultiItemCarousel";
// import { useDispatch, useSelector } from "react-redux";

export const Home = () => {
  //   const dispatch = useDispatch();
  //   const jwt = localStorage.getItem("jwt");
  //   const { restaurant } = useSelector((store) => store);
  //   console.log("restaurant", restaurant);
  //   useEffect(() => {
  //     dispatch(gettAllRestaurantsAction(jwt));
  //   }, []);
  return (
    <div className="pb-10">
      <section className="banner -z-50 relative flex flex-col justify-center items-center">
        <div className="w-[50vw] z-10 text-center">
          <p className="w-2x1 lg:text-6xl font-bold z-10 py-5">
            Vibrant Fresh Grub
          </p>
          <p className="z-10 text-gray-100 text-xl lg:text-4xl">
            Taste the Comfort Home Cook: Food, Fast and Delivered
          </p>
        </div>
        <div className="cover absolute top-0 left-0 right-0"></div>
        <div className="fadeout"></div>
      </section>
    </div>
  );
};
