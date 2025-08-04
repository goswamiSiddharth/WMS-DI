import React from 'react';
import { BrowserRouter, Routes, Route, NavLink } from 'react-router-dom';
import Home from './pages/Home';
import UploadPage from './pages/UploadPage';
import DashboardPage from './pages/DashboardPage';


function App() {
  return (
    <BrowserRouter>
      <header style={{ padding: '1rem', background: '#282c34', color: '#fff', display: 'flex', gap: '1rem' }}>
        <NavLink to="/" end style={({ isActive }) => ({ color: isActive ? '#61dafb' : '#fff' })}>Home</NavLink>
        <NavLink to="/upload" style={({ isActive }) => ({ color: isActive ? '#61dafb' : '#fff' })}>Upload</NavLink>
        <NavLink to="/dashboard" style={({ isActive }) => ({ color: isActive ? '#61dafb' : '#fff' })}>Dashboard</NavLink>
      </header>

      <main style={{ padding: '1rem' }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/upload" element={<UploadPage />} />
          <Route path="/dashboard" element={<DashboardPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
