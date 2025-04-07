import React from "react";
import ShoppingBasketIcon from "@mui/icons-material/ShoppingBasket";
import FavoriteIcon from "@mui/icons-material/Favorite";
import HomeIcon from "@mui/icons-material/Home";
import PaymentIcon from "@mui/icons-material/Payment";
import NotificationsNoneIcon from "@mui/icons-material/NotificationsNone";
import TodayIcon from "@mui/icons-material/Today";
import LogoutIcon from "@mui/icons-material/Logout";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { Divider } from "@mui/material";

const menu = [
  { titile: "Orders", icon: <ShoppingBasketIcon /> },
  { titile: "Favorites", icon: <FavoriteIcon /> },
  { titile: "Address", icon: <HomeIcon /> },
  { titile: "Payment", icon: <PaymentIcon /> },
  { titile: "Notification", icon: <NotificationsNoneIcon /> },
  { titile: "Coupons", icon: <TodayIcon /> },
  { titile: "Logout", icon: <LogoutIcon /> },
];

const ProfileNavigation = () => {
  const navigate = useNavigate();
  const disptch = useDispatch();
  const handleNavigate = (item) => {
    if (item.titile === "Logout") {
      disptch(logout());
      navigate("/");
    } else navigate(`/my-profile/${item.titile.toLowerCase()}`);
  };

  return (
    <div>
      {/* <Drawer
        variant={isSmallScreen ? "temporary" : "permanent"}
        onClose={handleClose} // Drawer를 닫는 함수
        open={isSmallScreen ? open : true}
        // open={true} // Drawer 열려있는지 여부를 제어
        anchor="left" // Drawer를 화면의 왼쪽에 위치
        sx={{ zIndex: -1, position: "sticky" }} // 다른 요소들 위에 표시되도록 zIndex:1
      > */}
      <div className="w-[50vw] lg:w-[20vw] h-[100vh] flex flex-col justify-center text-xl pt-16 gap-8">
        {/* 목록 시작 */}
        {menu.map((item, i) => (
          <>
            <div
              onClick={() => handleNavigate(item)}
              className="px-5 flex items-center space-x-5 cursor-pointer"
            >
              {item.icon}
              <span>{item.titile}</span>
            </div>
            {/* 모든 항목 뒤에 Divider 구분선-> 마지막은 Divider가 나타나지 않도록 -1 */}
            {i !== menu.length - 1 && <Divider />}
          </>
        ))}
      </div>
      {/* </Drawer> */}
    </div>
  );
};

export default ProfileNavigation;
