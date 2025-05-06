import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import searchIcon from '../assets/images/search_icon.png';
import lineIcon from '../assets/images/line_icon.png';
import '../css/SearchBar.css';

const SearchBar = () => {
  const [query, setQuery] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    if (query.trim()) {
      navigate(`/search?query=${encodeURIComponent(query.trim())}`);
      setQuery('');
    }
  };

  return (
    <form className="search-bar" onSubmit={handleSearch}>
      <div className="search-input-container">
        <input
          type="text"
          placeholder="Поиск..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="search-input"
        />
        <img src={lineIcon} alt="Line" className="line-icon" />
        <img
          src={searchIcon}
          alt="Search"
          className="search-button-icon"
          onClick={handleSearch}
        />
      </div>
    </form>
  );
};

export default SearchBar;