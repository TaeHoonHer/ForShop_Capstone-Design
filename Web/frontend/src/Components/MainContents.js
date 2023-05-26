import React, { useEffect, useRef, useState } from 'react';
import '../Css/MainContents.css';
import { Link } from 'react-router-dom';

function MainContents({ selectedKeyword, searchValue }) {
  const conimages = [
    { src: '/img/a1.jpg', id: 1, title: 'image1', content: '하이요1', keyword: '산 모두들' },
    { src: '/img/a2.jpg', id: 2, title: 'image2', content: '하이요2', keyword: '산 모두들' },
    { src: '/img/a3.jpg', id: 3, title: 'image3', content: '하이요3', keyword: '산 모두들' },
    { src: '/img/a4.jpg', id: 4, title: 'image4', content: '하이요4', keyword: '산 모두들' },
    { src: '/img/a5.jpg', id: 5, title: 'image5', content: '하이요5', keyword: '산 모두들' },
    { src: '/img/a6.jpg', id: 6, title: 'image6', content: '하이요6', keyword: '산 모두들' },
    { src: '/img/a7.jpg', id: 7, title: 'image7', content: '하이요7', keyword: '산 모두들' },
    { src: '/img/a8.jpg', id: 8, title: 'image8', content: '하이요8', keyword: '산 모두들' },
    { src: '/img/b1.jpg', id: 9, title: 'image9', content: '하이요9', keyword: '산 모두들' },
    { src: '/img/b2.jpg', id: 10, title: 'image10', content: '하이요10', keyword: '산 모두들' },
    { src: '/img/b3.jpg', id: 11, title: 'image11', content: '하이요11', keyword: '산 모두들' },
    { src: '/img/b4.jpg', id: 12, title: 'image12', content: '하이요12', keyword: '산 모두들' },
    /* 서버에서 업로드 이미지 파일들을 받을 곳 */
  ];

  const filteredImages = searchValue === ''
    ? conimages
    : conimages.filter((image) => {
        if (selectedKeyword === 'ID') {
          return String(image.id) === searchValue;
        } else if (selectedKeyword === 'Keyword') {
          return image.keyword.includes(searchValue);
        } else {
          return true; // 선택한 키워드가 없을 경우, 모든 이미지를 표시합니다.
        }
  });

  const containerRef = useRef(null);
  const [visibleImages, setVisibleImages] = useState(8); // 초기에 보여지는 이미지 수
  const [hoveredImage, setHoveredImage] = useState(null);

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

  const handleImageMouseEnter = (image) => {
    setHoveredImage(image);
  };

  const handleImageMouseLeave = () => {
    setHoveredImage(null);
  };

  return (
    <div className="MainContents">
      <div className="ContentsContainer" ref={containerRef}>
        {filteredImages.slice(0, visibleImages).map((image, index) => (
          <div className={`bg${index + 2} image-container`} key={index}>
            <Link to={`/main/imgboard/?id=${image.id}&title=${image.title}&src=${image.src}&content=${image.content}&keyword=${image.keyword}`}>
              <img 
                src={image.src} 
                alt={image.alt} 
                id='imageList' 
                onMouseEnter={() => handleImageMouseEnter(image)}
                onMouseLeave={handleImageMouseLeave}
              />
            </Link>
            {hoveredImage === image && (
              <div className='imageInfo'>
                <div>{`ID: ${image.id}`}</div>
                <div>{`Title: ${image.title}`}</div>
              </div>
            )}
          </div>
        ))}
      </div>
      {visibleImages < filteredImages.length && (
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