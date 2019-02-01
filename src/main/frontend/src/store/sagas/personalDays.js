import { call, put, takeLatest } from 'redux-saga/effects';
import { getPersonalDaysApi } from '../services/personalDaysApi';
import moment from 'moment';
import { initDate } from '../actions/calendarActions';
import {
  errorReceivingPersonalDays,
  receivePersonalDays,
  requestPersonalDays
} from '../actions/personalDaysActions';

export const getPersonalDays = function*() {
  try {
    const personalDays = yield call(getPersonalDaysApi);
    yield put(receivePersonalDays(personalDays));
    const now = moment();
    yield put(
      initDate({
        now: now,
        months: moment.months(),
        year: now.year(),
        currentMonth: moment.months(now.month()),
        currentDay: moment().date(),
        personalDays: personalDays
      })
    );
  } catch (error) {
    yield put(errorReceivingPersonalDays(error));
  }
};

export const personalDaysSaga = [
  takeLatest(requestPersonalDays, getPersonalDays)
];
