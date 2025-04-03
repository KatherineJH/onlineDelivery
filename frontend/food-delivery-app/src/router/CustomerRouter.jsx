import React from 'react'
import { Route, Routes } from 'react-router-dom'
import { Navbar } from '../customerComponent/navbar/Navbar'
import { Home } from '../customerComponent/home/Home'

export const CustomerRouter = () => {
  return (
    <div>
      <Navbar/>
      <Routes>
        <Route path="/" element={<Home />}/>
      </Routes>
    </div>
  )
}
