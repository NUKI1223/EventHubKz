import React, { useState, useEffect, useRef } from 'react';
import api from '../api';
import '../css/TagSelector.css';

const TagSelector = ({ selectedTags, onChange }) => {
  const [tags, setTags] = useState([]);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const fetchTags = async () => {
      try {
        const res = await api.get('/api/tags');
        setTags(res.data);
      } catch (err) {
        console.error('Ошибка загрузки тегов:', err);
        setError('Ошибка загрузки тегов');
      } finally {
        setLoading(false);
      }
    };
    fetchTags();
  }, []);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setDropdownOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const toggleTag = (tagName) => {
    const newSelected = selectedTags.includes(tagName)
      ? selectedTags.filter((t) => t !== tagName)
      : [...selectedTags, tagName];
    onChange(newSelected);
  };

  return (
    <div className="tag-selector" ref={dropdownRef}>
      <div
        className="tag-selector-input"
        onClick={() => setDropdownOpen(!dropdownOpen)}
      >
        {selectedTags.length > 0 ? selectedTags.join(', ') : 'Выберите тэги'}
      </div>
      {dropdownOpen && (
        <div className="tag-selector-list">
          {loading && <p>Загрузка...</p>}
          {error && <p>{error}</p>}
          {!loading &&
            !error &&
            tags.map((tag) => (
              <label key={tag.id} className="tag-item">
                <input
                  type="checkbox"
                  value={tag.name}
                  checked={selectedTags.includes(tag.name)}
                  onChange={() => toggleTag(tag.name)}
                />
                {tag.name}
              </label>
            ))}
        </div>
      )}
    </div>
  );
};

export default TagSelector;
