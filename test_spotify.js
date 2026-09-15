const https = require('https');

const postData = 'grant_type=client_credentials&client_id=1b00cfecc0634cb98a65b26e55d94896&client_secret=c662df00126f44669436364c4d58c7c9';

const options = {
  hostname: 'accounts.spotify.com',
  port: 443,
  path: '/api/token',
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Content-Length': postData.length
  }
};

const req = https.request(options, (res) => {
  let data = '';
  res.on('data', (chunk) => data += chunk);
  res.on('end', () => {
    const token = JSON.parse(data).access_token;
    
    const searchOptions = {
      hostname: 'api.spotify.com',
      port: 443,
      path: '/v1/search?q=artist%3Aconfetti%20weather&type=track&limit=40',
      method: 'GET',
      headers: { 'Authorization': 'Bearer ' + token }
    };
    
    const searchReq = https.request(searchOptions, (searchRes) => {
      let searchData = '';
      searchRes.on('data', (chunk) => searchData += chunk);
      searchRes.on('end', () => console.log(searchData.substring(0, 500)));
    });
    searchReq.end();
  });
});
req.write(postData);
req.end();
