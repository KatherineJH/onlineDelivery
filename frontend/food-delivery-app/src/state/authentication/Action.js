import axios from "axios";
import { api, API_URL } from "../config/Api";
import {
  // ADD_TO_FAVORITE_REQUEST,
  // ADD_TO_FAVORITE_SUCCESS,
  // ADD_TO_FAVORITE_FAILURE,
  LOGIN_SUCCESS,
  LOGIN_FAILURE,
  LOGOUT,
  REGISTER_FAILURE,
  REGISTER_REQUEST,
  REGISTER_SUCCESS,
  GET_USER_REQUEST,
  GET_USER_SUCCESS,
  GET_USER_FAILURE,
  LOGIN_REQUEST,
} from "./ActionType";

/**
 * 참고용 예제 코드
 * 변수 및 api end-point 다를 것이므로 수정이 필요함.
 * */
export const registerUser = (reqData) => async (dispatch) => {
  dispatch({ type: REGISTER_REQUEST });
  try {
    const { data } = await axios.post(
      `${API_URL}/api/auth/register`,
      reqData.userData
    );
    if (data.jwt) localStorage.setItem("jwt", data.jwt);
    if (data.role === "ROLE_RESTAURANT_OWNER") {
      reqData.navigate("/admin/restaurant");
    } else {
      reqData.navigate("/");
    }
    dispatch({ type: REGISTER_SUCCESS, payload: data.jwt });
    console.log("registered successfully", data);
  } catch (error) {
    dispatch({ type: REGISTER_FAILURE, payload: error });
    console.log("error", error);
  }
};

/**
 * 참고용 예제 코드
 * 변수 및 api end-point 다를 것이므로 수정이 필요함.
 * */
export const loginUser = (reqData) => async (dispatch) => {
  dispatch({ type: LOGIN_REQUEST });
  try {
    const { data } = await axios.post(
      `${API_URL}/api/auth/login`,
      reqData.userData
    );
    if (data.jwt) localStorage.setItem("jwt", data.jwt);
    if (data.role === "ROLE_RESTAURANT_OWNER") {
      reqData.navigate("/admin/restaurant");
    } else {
      reqData.navigate("/");
    }
    dispatch({ type: LOGIN_SUCCESS, payload: data.jwt });
    console.log("logged in successfully", data);
  } catch (error) {
    dispatch({ type: LOGIN_FAILURE, payload: error });

    console.log("error", error);
  }
};

/**
 * 참고용 예제 코드
 * 변수 및 api end-point 다를 것이므로 수정이 필요함.
 * */
export const getUser = (jwt) => async (dispatch) => {
  dispatch({ type: GET_USER_REQUEST });
  try {
    const { data } = await api.get(`${API_URL}/api/auth/me`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    console.log("user profile", data);
    dispatch({ type: GET_USER_SUCCESS, payload: data });
  } catch (error) {
    console.log("error", error);
    dispatch({ type: GET_USER_FAILURE, payload: error });
  }
};

//   export const addToFavorite =
//     ({ jwt, restaurantId }) =>
//     async (dispatch) => {
//       dispatch({ type: ADD_TO_FAVORITE_REQUEST });
//       try {
//         const { data } = await axios.put(
//           `${API_URL}/api/restaurant/${restaurantId}/add-favorite`,
//           {},
//           {
//             headers: {
//               Authorization: `Bearer ${jwt}`,
//             },
//           }
//         );
//         dispatch({ type: ADD_TO_FAVORITE_SUCCESS, payload: data });
//         console.log("added to favorite successfully", data);
//       } catch (error) {
//         dispatch({ type: ADD_TO_FAVORITE_FAILURE, payload: error });
//         console.log("error", error);
//       }
//     };

/**
 * 참고용 예제 코드
 * 변수 및 api end-point 다를 것이므로 수정이 필요함.
 * */
export const logout = () => async (dispatch) => {
  try {
    localStorage.clear();
    dispatch({ type: LOGOUT });
    console.log("logged out successfully");
  } catch (error) {
    console.log("error", error);
  }
};
