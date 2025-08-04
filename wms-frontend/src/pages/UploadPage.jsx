import React, { useState } from 'react';
import FileUpload from '../components/FileUpload';

export default function UploadPage() {
  const [summary, setSummary] = useState(null);

  return (
    <div>
      <h2>Upload Sales Data</h2>
      <FileUpload
        onSuccess={(data) => {
          setSummary(data);
        }}
      />
      {summary && (
        <div style={{ marginTop: '1rem', color: 'green' }}>
          <strong>Upload Summary:</strong>
          <p>📂 Filename: {summary.filename}</p>
          <p>✅ Mapped SKUs: {summary.mappedCount}</p>
          <p>🆕 New SKUs: {summary.newCount}</p>
          <p>Status: {summary.status}</p>
        </div>
      )}
    </div>
  );
}
