import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function LogsTable() {
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    axios.get('/api/logs')
    .then(res => setLogs(res.data))
    .catch(err => console.error('Failed to load logs:', err));
    // console.log('Logs from backend:', res.data);
  }, []);

  return (
    <div style={{ marginTop: '2rem' }}>
      <h3>Mapping Logs</h3>
      {logs.length === 0
        ? <p>No errors in mapping.</p>
        : (
          <table style={{ borderCollapse: 'collapse', width: '100%' }}>
            <thead>
              <tr>
                <th>ID</th>
                <th>SKU</th>
                <th>Product</th>
                <th>Message</th>
                <th>Timestamp</th>
              </tr>
            </thead>
            <tbody>
              {logs.map(l => (
                <tr key={l.id}>
                  <td>{l.id}</td>
                  <td>{l.sku}</td>
                  <td>{l.product}</td>
                  <td>{l.message}</td>
                  <td>{new Date(l.timestamp).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )
      }
    </div>
  );
}
