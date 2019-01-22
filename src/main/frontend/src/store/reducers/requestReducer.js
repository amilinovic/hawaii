import { handleActions } from 'redux-actions';
import {
  openRequestPopup,
  closeRequestPopup,
  selectRequestType,
  selectAbsenceType
} from '../actions/requestActions';

export const initialState = {
  openPopup: false,
  data: {}
};

const actionHandlers = {
  [openRequestPopup]: state => ({ ...state, openPopup: true }),
  [closeRequestPopup]: () => ({ ...initialState }),
  [selectRequestType]: (state, action) => ({
    ...state,
    requestType: action.payload
  }),
  [selectAbsenceType]: (state, action) => ({
    ...state,
    data: { ...state.data, absence: action.payload }
  })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
