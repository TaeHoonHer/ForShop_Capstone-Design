import '../Css/MainContents2.css';
import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

function MainContents2({ selectedKeyword, searchValue }) {
    const [visibleVideos, setVisibleVideos] = useState(8);
    const [convideos, setConvideos] = useState([]);

    useEffect(() => {
        const fetchAccessToken = async () => {
            try {
                const accessToken = localStorage.getItem('accessToken');

                const videoResponse = await axios.get('/api/videos', {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`
                    }
                });
        
                const videoExtensions = ['mp4', 'avi', 'mkv', 'mov', 'flv'];
                const filteredVideos = videoResponse.data.filter(video => {
                    const extension = video.src.split('.').pop();
                    return videoExtensions.includes(extension);
                });
                setConvideos(filteredVideos);
            } catch (error) {
                console.error(error);
            }
        }

        fetchAccessToken();
    }, []);

    const filteredVideos = searchValue === ''
    ? convideos
    : convideos.filter((video) => {
        if (selectedKeyword === 'ID') {
          return String(video.id) === searchValue;
        } else if (selectedKeyword === 'Keyword') {
          return video.keyword.includes(searchValue);
        } else {
          return true; // 선택한 키워드가 없을 경우, 모든 이미지를 표시합니다.
        }
    });

    const containerRef = useRef(null);

    useEffect(() => {
        const container = containerRef.current;
        const videoElems = container.querySelectorAll('video');

        videoElems.forEach((video, index) => {
            video.style.animationDelay = `${index * 100}ms`;
            video.classList.add('fadeup');
        });
    }, [visibleVideos]);

    const handleShowMore = () => {
        setVisibleVideos(prevVisibleVideos => prevVisibleVideos + 8);
    };

    return (
        <div className='MainContents'>
            <div className='ContentsContainer' ref={ containerRef }>
                {filteredVideos.slice(0, visibleVideos).map((video, index) => (
                  <div className={`bg${index + 2}`} key={index}>
                    <Link to={`/main/vdboard/?id=${video.id}&title=${video.title}&src=${video.src}&content=${video.content}&keyword=${video.keyword}`}>
                        <video
                            src={video.src}
                            alt={video.alt} autoPlay loop muted />
                    </Link>
                  </div>
                ))}
            </div>
            {visibleVideos < filteredVideos.length && (
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