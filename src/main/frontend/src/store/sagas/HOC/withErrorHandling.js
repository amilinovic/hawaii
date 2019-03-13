import { push } from 'connected-react-router';
import { put } from 'redux-saga/effects';
import { toastrError } from '../toastrHelperSaga';

export function withErrorHandling(saga, errorHandler) {
  return function* sagaWithErrorHandler(...args) {
    try {
      // yield* iterates over a generator and yields every value from it
      // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/yield*
      yield* saga(...args);
    } catch (e) {
      if (e.status === 401) {
        yield put(push('/login'));
      }
      yield errorHandler(e);
      yield put(toastrError(e.message));
    }
  };
}

export const genericErrorHandler = action =>
  function*(e) {
    yield put(action(e));
  };
