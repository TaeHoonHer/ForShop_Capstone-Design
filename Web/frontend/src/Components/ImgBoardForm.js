import React, { useState, useEffect } from 'react';
import '../Css/ImgBoardForm.css';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';

function ImgBoardForm({ imageId }) {
  const [image, setImage] = useState({});
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const { articleId } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    console.log(articleId);
    if (!articleId) return;
    const accessToken = localStorage.getItem('accessToken');

    console.log("get 요청 시작");
    axios.get(`/api/articles/${articleId}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }).then((response) => {
      setImage(response.data);
      console.log(image.articleCommentResponse);
    })
    .catch(error => {
      console.error(error);
    });

   }, [imageId]);

  const handleCommentSubmit = (event) => {
    event.preventDefault();

    const accessToken = localStorage.getItem('accessToken');

    axios.post('/api/comments/new', {
                   articleId : articleId,
                   content : newComment
                 }, {
      headers: {
        'Authorization': `Bearer ${accessToken}`
      }
    }).then(response => {
      if (response.status === 200) {
      }
    })
    .catch(error => {
      console.error(error);
    });
  };

  const handleCommentChange = (event) => {
    setNewComment(event.target.value);
  }

  if (!articleId) {
    navigate("/main");
  };

  return (
    <div className='ImgboardWrapper'>
      <div className='boardBox'>
        <div className='imgContainer'>
          <div className='imgBox'>
            <img src={"https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/" + image.storedName} alt={image.title} />
          </div>
        </div>
        <div className='chatbox'>
          <div className='idBox'>
            <img src="/img/user.png" />
            <p>{image.nickname}</p>
          </div>
          <div className='titleBox'>
            <h2>{image.title}</h2>
          </div>
          <div className='ctBox'>
            <p>{image.content}</p>
          </div>
          <div className='ch'>
          {image.articleCommentResponse ? Object.entries(image.articleCommentResponse).map(([key, comment]) => (
            <div className='comment' key={key}>
              <img src="/img/user.png" alt="User" className="commentUserImg" />
              {comment.nickname}&nbsp;&nbsp;&nbsp;&nbsp;{comment.content}
            </div>
            )) : null}
            <form onSubmit={handleCommentSubmit}>
              <input
                type="text"
                value={newComment}
                onChange={handleCommentChange}
                placeholder="Write a comment..."
              />
              <button type="submit">남기기</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ImgBoardForm
