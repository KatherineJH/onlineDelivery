import { applyMiddleware, combineReducers, legacy_createStore } from "redux";
import { thunk } from "redux-thunk";
import { authReducer } from "./authentication/Reducer";

const rootReducer = combineReducers({
  auth: authReducer,
  // restaurant: restaurantReducer,
  // menu: menuItemReducer,
  // cart: cartReducer,
  // order: orderReducer,
  // adminRestaurantOrder: adminRestaurantsOrderReducer,
});
export const store = legacy_createStore(rootReducer, applyMiddleware(thunk));
