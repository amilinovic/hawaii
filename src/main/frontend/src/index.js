import React from 'react';
import ReactDOM from 'react-dom';
import 'reset-css/reset.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import './styles/globalStyles.js';

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
