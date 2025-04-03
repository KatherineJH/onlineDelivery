import React from "react";
import { Avatar, Badge, Box, IconButton } from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import { purple } from "@mui/material/colors";
import { Person, ShoppingCart } from "@mui/icons-material";
import "./Navbar.css";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

export const Navbar = () => {
  // const { auth, cart } = useSelector((store) => store);
  const navigate = useNavigate();
  const handleClickAvatar = () => {
    // if (auth.user?.role === "ROLE_CUSTOMER") {
    navigate("/my-profile");
    // } else {
    //   navigate("/admin/restaurant");
    // }
  };
  return (
    <Box className="px-5 sticky top-0 z-50 py-[1.8rem] bg-[#a885ed] lg:px-20 flex justify-between">
      <div className="lg:mr-10 cursor-pointer flex items-center space-x-4">
        <li
          onClick={() => navigate("/")}
          className="logo font-semibold text-white text-2xl"
        >
          BIT FOOD SERVICE
        </li>
        <div className="flex items-center space-x-2 lg:space-x-10">
          <div className="">
            <IconButton>
              <SearchIcon sx={{ fontSize: "1.5rem" }} />
            </IconButton>
          </div>
          <div className="">
            <Avatar
              onClick={handleClickAvatar}
              sx={{ bgcolor: "white", color: purple.A700 }}
            >
              C
            </Avatar>
          </div>
        </div>
        <div className="">
          <IconButton onClick={() => navigate("/cart")}>
            <Badge
              color="secondary"
              //   badgeContent={cart.cart?.item?.length || 0}
            >
              <ShoppingCart sx={{ fontSize: "1.5rem" }} />
            </Badge>
          </IconButton>
        </div>
      </div>
    </Box>
  );
};
