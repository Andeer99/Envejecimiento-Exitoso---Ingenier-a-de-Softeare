// src/components/Pagination.jsx
import React from 'react';
import '../css/pagination.css';

export default function Pagination({ currentPage, totalPages, onPageChange }) {
  const pages = Array.from({ length: totalPages }, (_, i) => i+1);
  return (
    <ul className="pagination">
      <li
        className={currentPage===1 ? 'disabled' : ''}
        onClick={()=>currentPage>1 && onPageChange(currentPage-1)}
      >
        « Anterior
      </li>
      {pages.map(n => (
        <li
          key={n}
          className={n===currentPage ? 'active' : ''}
          onClick={()=>onPageChange(n)}
        >
          {n}
        </li>
      ))}
      <li
        className={currentPage===totalPages ? 'disabled' : ''}
        onClick={()=>currentPage<totalPages && onPageChange(currentPage+1)}
      >
        Siguiente »
      </li>
    </ul>
  )
}
