import { createStore, applyMiddleware, compose } from 'redux';
import createSagaMiddleware from 'redux-saga';

import reducer from './reducers/';
import mySaga from './sagas/sagas';

const sagaMiddleware = createSagaMiddleware();

export default createStore(
  reducer,
  compose(
    applyMiddleware(sagaMiddleware)
    // Comment out this line before opening (testing) the app in browser
    // without redux dev tools installed, for example when demoing the app
    // window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
  )
);

sagaMiddleware.run(mySaga);
