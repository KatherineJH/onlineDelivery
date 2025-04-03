import React from "react";
import { Avatar, Badge, Box, IconButton } from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import { purple } from "@mui/material/colors";
import { ShoppingCart } from "@mui/icons-material";
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
      <div className="flex items-center space-x-4 cursor-pointer">
        <div
          onClick={() => navigate("/")}
          className="logo font-semibold text-white text-2xl"
        >
          BIT FOOD SERVICE
        </div>
      </div>

      <div className="flex items-center space-x-2 lg:space-x-10">
        <div>
          <IconButton>
            <SearchIcon sx={{ fontSize: "1.5rem" }} />
          </IconButton>
        </div>

        <div>
          <Avatar
            onClick={handleClickAvatar}
            sx={{ bgcolor: "white", color: purple.A700 }}
          >
            C
          </Avatar>
        </div>

        <div>
          <IconButton onClick={() => navigate("/cart")}>
            <Badge
              color="secondary"
              // badgeContent={cart.cart?.item?.length || 0} // number of items in my cart
            >
              <ShoppingCart sx={{ fontSize: "1.5rem" }} />
            </Badge>
          </IconButton>
        </div>
      </div>
    </Box>
  );
};
