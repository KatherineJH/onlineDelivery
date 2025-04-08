import React, { useState } from "react";
import axios from "axios";

const SearchPage = () => {
  const [keyword, setKeyword] = useState("");
  const [results, setResults] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSearch = async () => {
    if (!keyword.trim()) return;

    try {
      setIsLoading(true);
      setError("");
      const res = await axios.get(`http://localhost:5353/api/products/search?name=${keyword}`);
      console.log("!!!!!!!!! 응답 결과:", res.data);
      setResults(res.data);
    } catch (err) {
      console.error("검색 실패:", err);
      setError("검색 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") handleSearch();
  };

  return (
    <div className="min-h-[80vh] p-6">
      <h2 className="text-2xl font-semibold mb-6"> 검색</h2>

      <div className="flex items-center mb-4">
        <input
          type="text"
          placeholder="검색어를 입력하세요"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyPress={handleKeyPress}
          className="border px-4 py-2 rounded-md w-full max-w-md mr-2"
        />
        <button
          onClick={handleSearch}
          className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700"
        >
          검색
        </button>
      </div>

      {isLoading && <p>검색 중입니다...</p>}
      {error && <p className="text-red-500">{error}</p>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mt-4">
  {results.map((item) => (
    <div
      key={item.productId}
      className="border p-4 rounded-md shadow hover:shadow-lg transition"
    >
      <h3 className="text-xl font-bold">{item.name}</h3>
      <p className="text-sm text-white">가격: {item.price}원</p>
      <p className="text-sm text-white">가게: {item.store.name}</p>
      <p className="text-sm text-white">위치: {item.store?.location || "정보 없음"}</p>
      <p className="text-sm text-white">카테고리: {item.category.name}</p>
    </div>
  ))}
</div>

    
    </div>
  );
};

export default SearchPage;
