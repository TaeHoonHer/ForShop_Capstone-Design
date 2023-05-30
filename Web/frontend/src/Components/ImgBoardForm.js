import React, { useState, useEffect } from 'react';
import '../Css/ImgBoardForm.css';
import axios from 'axios';

function ImgBoardForm({ imageId }) {
  const [image, setImage] = useState({});
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');

  useEffect(() => {
    if (!imageId) return;
    const accessToken = localStorage.getItem('accessToken');

    axios.get(`/api/imgboard/${imageId}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }).then(response => {
      setImage(response.data);
    })
    .catch(error => {
      console.error(error);
    });

    axios.get(`/api/comments/${imageId}`, {
    }).then(response => {
      if (response.status === 200) {
        setComments(response.data.articleId);
      }
    })
    .catch(error => {
      console.error(error);
    });
  }, [imageId]);

  const handleCommentSubmit = (event) => {
    event.preventDefault();

    const accessToken = localStorage.getItem('accessToken');

    axios.post(`/api/comments/${imageId}`, { text: newComment }, {
      headers: {
        'Authorization': `Bearer ${accessToken}`
      }
    }).then(response => {
      if (response.status === 200) {
        setComments([...comments, response.data]);
        setNewComment('');
      }
    })
    .catch(error => {
      console.error(error);
    });
  };

  const handleCommentChange = (event) => {
    setNewComment(event.target.value);
  }

  if (!imageId) return null;

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
            <p>{image.id}</p>
          </div>
          <div className='titleBox'>
            <h2>{image.title}</h2>
          </div>
          <div className='ctBox'>
            <p>{image.content}</p>
          </div>
          <div className='ch'>
            {comments.map((comment, index) => (
              <div key={index} className='comment'>
                <img src="/img/user.png" alt="User" className="commentUserImg" />
                {comment.text}
              </div>
            ))}
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
