import { handleActions } from 'redux-actions';
import {
  openRequestPopup,
  closeRequestPopup,
  selectRequestType,
  selectAbsenceType,
  selectStartDate,
  selectEndDate
} from '../actions/requestActions';

export const initialState = {
  openPopup: false,
  data: {},
  startDate: new Date(),
  endDate: new Date()
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
  }),
  [selectStartDate]: (state, action) => ({
    ...state,
    startDate: action.payload
  }),
  [selectEndDate]: (state, action) => ({ ...state, endDate: action.payload })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
