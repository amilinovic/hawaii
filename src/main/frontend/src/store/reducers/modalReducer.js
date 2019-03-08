import { handleActions } from 'redux-actions';
import { closeModal, resetModalState } from '../actions/modalActions';

export const initialState = {
  shouldClose: false
};

const actionHandlers = {
  [closeModal]: state => {
    return {
      ...state,
      shouldClose: true
    };
  },
  [resetModalState]: state => {
    return {
      ...state,
      shouldClose: false
    };
  }
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
