import React, { useEffect, useRef, useState } from 'react';
import '../Css/MainContents.css';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

function MainContents() {
  const conimages = [
    { src: '/img/a1.jpg', alt: 'image1' },
    { src: '/img/a2.jpg', alt: 'image2' },
    { src: '/img/a3.jpg', alt: 'image3' },
    { src: '/img/a4.jpg', alt: 'image4' },
    { src: '/img/a5.jpg', alt: 'image5' },
    { src: '/img/a6.jpg', alt: 'image6' },
    { src: '/img/a7.jpg', alt: 'image7' },
    { src: '/img/a8.jpg', alt: 'image8' },
    { src: '/img/b1.jpg', alt: 'image9' },
    { src: '/img/b2.jpg', alt: 'image10' },
    { src: '/img/b3.jpg', alt: 'image11' },
    { src: '/img/b4.jpg', alt: 'image12' },
    /* 서버에서 업로드 이미지 파일들을 받을 곳 */
  ];

  const containerRef = useRef(null);
  const [visibleImages, setVisibleImages] = useState(8); // 초기에 보여지는 이미지 수

  useEffect(() => {
    const container = containerRef.current;
    const imageElems = container.querySelectorAll('img');

    imageElems.forEach((img, index) => {
      img.style.animationDelay = `${index * 100}ms`;
      img.classList.add('fadeup');
    });
  }, [visibleImages]); // visibleImages 변경 시에만 실행되도록 설정

  const handleShowMore = () => {
    setVisibleImages(prevVisibleImages => prevVisibleImages + 8); // 8개씩 추가로 보여주도록 업데이트
  };

  const handleImageMouseEnter = (e, image) => {
    e.target.classList.add('image-hover');
    e.target.setAttribute('data-userId', image.userID);
    e.target.setAttribute('data-title', image.title);
  };

  const handleImageMouseLeave = (e) => {
    e.target.classList.remove('image-hover');
    e.target.removeAttribute('data-userId');
    e.target.removeAttribute('data-title');
  };

  return (
    <div className="MainContents">
      <div className="ContentsContainer" ref={containerRef}>
        {conimages.slice(0, visibleImages).map((image, index) => (
          <div className={`bg${index + 2}`} key={index}>
            <Link to = '/main/board'>
              <img src={image.src} alt={image.alt} />
            </Link>
          </div>
        ))}
      </div>
      {visibleImages < conimages.length && (
        <div className='ButtonContainer'>
          <button className="ShowMoreButton" onClick={handleShowMore}>
            더 보기
          </button>
        </div>
      )}
    </div>
  );
}

export default MainContents;