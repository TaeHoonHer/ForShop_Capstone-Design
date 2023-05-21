import '../Css/MainContents2.css';
import { useEffect, useRef, useState } from 'react';

function MainContents2() {
    const [visibleVideos, setVisibleVideos] = useState(8);
    const convideos = [
        { src: '/img/dance.mp4', alt: 'video1' },
        { src: '/img/intro3.mp4', alt: 'video2' },
        { src: '/img/banner.mp4', alt: 'video3' },
        { src: '/img/new1.mp4', alt: 'video4' },
        { src: '/img/new2.mp4', alt: 'video5' },
        { src: '/img/v1.mp4', alt: 'video6' },
        { src: '/img/v2.mp4', alt: 'video7' },
        { src: '/img/v3.mp4', alt: 'video8' },
        { src: '/img/v4.mp4', alt: 'video9' },
        { src: '/img/v5.mp4', alt: 'video10' },
    ];

    const handleShowMore = () => {
        setVisibleVideos(prevVisibleVideos => prevVisibleVideos + 8);
    };

    const containerRef = useRef(null);

    useEffect(() => {
        const container = containerRef.current;
        const videoElems = container.querySelectorAll('video');

        videoElems.forEach((video, index) => {
            video.style.animationDelay = '${index * 100}ms';
            video.classList.add('fadeup');
        });
    }, [visibleVideos]);

    return (
        <div className='MainContents'>
            <div className='ContentsContainer' ref={ containerRef }>
                {convideos.slice(0, visibleVideos).map((video, index) => (
                  <div className={`bg${index + 2}`} key={index}>
                    <video src={video.src} alt={video.alt} autoPlay loop muted />
                  </div>
                ))}
            </div>
            {visibleVideos < convideos.length && (
                <div className='ButtonContainer'>
                  <button className="ShowMoreButton" onClick={handleShowMore}>
                    더 보기
                  </button>
                </div>
            )}
        </div>
    )
}

export default MainContents2;