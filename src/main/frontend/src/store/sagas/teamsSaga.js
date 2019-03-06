import { call, put, takeLatest } from 'redux-saga/effects';
import {
  errorReceivingTeams,
  receiveTeams,
  requestTeams
} from '../actions/teamsActions';
import { getTeamsApi } from '../services/teamsService';
import { toastrError } from './toastrHelperSaga';

export const getAllTeams = function*() {
  try {
    const teamsInformation = yield call(getTeamsApi);
    yield put(receiveTeams(teamsInformation));
  } catch (error) {
    yield put(errorReceivingTeams(error));
    yield put(toastrError(error.message));
  }
};

export const teamsSaga = [takeLatest(requestTeams, getAllTeams)];
