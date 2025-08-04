import React, { useState } from 'react';
import axios from 'axios';

export default function FileUpload({ onSuccess }) {
  const [file, setFile] = useState(null);
  const [progress, setProgress] = useState(0);
  const [error, setError] = useState('');

  const handleUpload = async e => {
    e.preventDefault();
    if (!file) return;

    setError('');
    setProgress(0);
    const formData = new FormData();
    formData.append('file', file);

    try {
      const resp = await axios.post('/api/upload', formData, {
        onUploadProgress: p => setProgress(Math.round((p.loaded * 100) / p.total)),
      });
      onSuccess && onSuccess(resp.data);
    } catch (err) {
       const responseData = err.response?.data;
      let message = err.message || 'Unknown error';

      if (typeof responseData === 'string') {
        message = responseData;
      } else if (typeof responseData === 'object') {
        message = (responseData.message)
          ? responseData.message
          : JSON.stringify(responseData);
      }

      setError(message);
    }
  };

  return (
    <form onSubmit={handleUpload}>
      <input
        type="file"
        accept=".xlsx,.xls,.csv"
        onChange={e => setFile(e.target.files[0])}
        required
      />
      <button type="submit">Upload</button>

      {progress > 0 && <p>Uploading: {progress}%</p>}
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}
    </form>
  );
}
