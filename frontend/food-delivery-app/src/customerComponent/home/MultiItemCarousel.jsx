import React from "react";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import Slider from "react-slick";
import { topMeals } from "./topMeals";
import CarouselItem from "./CarouselItem";

const MultiItemCarousel = () => {
    const settings = {
      dots: true,
      infinite: true,
      speed: 500,
      slidesToShow: 3,
      slideToScroll: 1,
      autoplay: true,
      autoplaySpeed: 2000,
      // arrows: false,
    };
  
    return (
      <div>
        <Slider {...settings}>
          {topMeals.map((item, index) => (
            <CarouselItem key={index} image={item.image} title={item.title} />
          ))}
        </Slider>
      </div>
    );
  };
  
  export default MultiItemCarousel;
  