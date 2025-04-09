import React, { useEffect, useState } from "react";
import axios from "axios";

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const jwt = localStorage.getItem("jwt");
        const res = await axios.get("http://localhost:5353/api/orders/my-orders", {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        });

        console.log("주문 응답:", res.data);

        if (Array.isArray(res.data)) {
          setOrders(res.data);
        } else {
          setOrders([]);
          setError("주문 데이터 형식이 잘못되었습니다.");
        }
      } catch (err) {
        console.error("❗ 주문 조회 실패:", err);
        setError("주문 내역을 불러오지 못했습니다.");
      }
    };

    fetchOrders();
  }, []);

  return (
    <div className="p-6 text-white min-h-screen">
      <h2 className="text-2xl font-bold mb-4"> 내 주문 내역</h2>

      {error && <p className="text-red-500 mb-4">{error}</p>}

      {orders.length === 0 && !error ? (
        <p className="text-gray-300">주문 내역이 없습니다.</p>
      ) : (
        <div className="space-y-6">
          {orders.map((order) => (
            <div key={order.orderId} className="border p-4 rounded shadow bg-[#2e2e2e]">
              <h3 className="text-lg font-semibold">🧾 주문번호: {order.orderId}</h3>
              <p>🏪 가게: {order.store?.name || "정보 없음"}</p>
              <p>🕒 주문일: {new Date(order.orderDate).toLocaleString()}</p>
              <p>📦 상태: {order.status}</p>
              <p>💰 총금액: {order.totalAmount.toLocaleString()}원</p>

              <div className="mt-2 ml-4">
                <p className="font-semibold">🍱 주문 상품:</p>
                <ul className="list-disc pl-6">
                  {order.orderItems?.map((item, idx) => (
                    <li key={idx}>
                      {item.product?.name || "상품 없음"} × {item.quantity}개 (
                      {item.price.toLocaleString()}원)
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Orders;
