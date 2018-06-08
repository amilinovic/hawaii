import React from 'react';
import ReactDOM from 'react-dom';
import 'reset-css/reset.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import { injectGlobal } from 'styled-components';

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();

injectGlobal`
    a {
        text-decoration: none;
    }
`;
