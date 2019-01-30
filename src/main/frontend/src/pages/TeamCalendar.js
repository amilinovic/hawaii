import React, { Component } from 'react';
import styled from 'styled-components';
import { bindActionCreators } from 'redux';
import {
  decrementMonth,
  incrementMonth,
  selectMonth
} from '../store/actions/teamCalendarActions';
import { requestUsers } from '../store/actions/teamCalendarActions';
import { connect } from 'react-redux';
import { getPublicHolidays, getUsers } from '../store/selectors';

import HolidayImg from '../img/holiday.svg';
import leaveImg from '../img/icons/leave_ss.png';
import bonusImg from '../img/icons/bonus_ss.png';
import sicknessImg from '../img/icons/sickness_ss.png';

import moment from 'moment';
import { requestPublicHolidays } from '../store/actions/publicHolidayActions';
import { ifProp, prop, switchProp } from 'styled-tools';
import {
  TeamCalendarUser,
  TeamCalendarUserName,
  TeamCalendarUserTh,
  TeamCalendarUserTd,
  TeamCalendarDay,
  TeamCalendarUserTr,
  TeamCalendarTotalsTd,
  GridLeaveImage,
  TeamCalendarGridTd,
  TeamCalendarWrapper,
  TeamCalendarMenus,
  TeamCalendarViewButton,
  TeamCalendarNavButton,
  TeamCalendarNavSelect,
  TeamCalendarContainer,
  TeamUsersContainer,
  TeamDaysTable,
  TeamTotalsTable,
  TeamCalendarTotalsTh,
  TeamCalendarGridContainer,
  TeamCalendarUserImage,
} from '../components/teamCalendar/TeamCalendarComponents'


const leaveTypes = {
  DEDUCTED_LEAVE: leaveImg,
  BONUS_DAYS: bonusImg,
  SICKNESS: sicknessImg,
  NON_DEDUCTED_LEAVE: leaveImg
};

const weekDays = ['S', 'M', 'T', 'W', 'T', 'F', 'S',];



class TeamCalendar extends Component {
  
  state = {
    currentMonth : moment().month() +1,
    currentYear : moment().year(),
    viewMode : 'LIST',
  }
  componentDidMount() {
    this.props.requestUsers();
    this.props.requestPublicHolidays();
  }

  getUsers = async () => {
    if (!this.props.users) {
      await this.props.requestUsers();
      return this.props.users;
    }
    return this.props.users;
  };

  getPublicHolidays = async () => {
    if (
      !this.props.publicHolidays ||
      (this.props.publicHolidays && this.props.publicHolidays.length) < 1
    ) {
      await this.props.requestPublicHolidays();
      return this.props.publicHolidays;
    }
    return this.props.publicHolidays;
  };

  renderTeamUsers = () => {
    var users = [];

    this.props.users.forEach(user => {
      users.push(
        <TeamCalendarUser key={'TeamCalendarUser' + user.id}>
          <TeamCalendarUserImage key={'TeamCalendarUserImage' + user.id} uid={user.id} />
          <TeamCalendarUserName key={'TeamCalendarUserName' + user.id}>
            {user.fullName}
          </TeamCalendarUserName>
        </TeamCalendarUser>
      );
    });

    return users;
  }

  renderDaysHeader = () => {
    var days = [];

    for ( var i = 1; i <= new Date(this.state.currentYear, this.state.currentMonth , 0).getDate(); i++) {
      days.push(
        <TeamCalendarUserTh key={'TeamCalendarUserTh' + i}>
          {weekDays[new Date(this.state.currentYear, this.state.currentMonth - 1, i).getDay()]}
        </TeamCalendarUserTh>
      );
    }
    return days;
  }

  renderDays = ()=> {
    var days = [];

    this.props.users.forEach(user => {
      var line = [];
      for (
        var j = 1;
        j <= new Date(this.state.currentYear, this.state.currentMonth, 0).getDate();
        j++
      ) {
        var personalDay = null;
        var publicHoliday = null;
        user.days.forEach(day => {
          if (
            day.date ===
            moment(new Date(this.state.currentYear, this.state.currentMonth - 1, j)).format(
              'YYYY-MM-DD'
            )
          ) {
            personalDay = day;
          }
        });
        this.props.publicHolidays.forEach(day => {
          if (
            day.date ===
            moment(new Date(this.state.currentYear, this.state.currentMonth - 1, j)).format(
              'YYYY-MM-DD'
            )
          ) {
            publicHoliday = day;
          }
        });

        line.push(
          <TeamCalendarUserTd key={'TeamCalendarUserTd' + j}>
            <TeamCalendarDay
              weekDay={
                weekDays[
                  new Date(this.state.currentYear, this.state.currentMonth - 1, j).getDay()
                ]
              }
              requestStatus={personalDay && personalDay.requestStatus}
            >
              {publicHoliday ? (
                <img src={HolidayImg} />
              ) : personalDay ? (
                <img src={leaveTypes[personalDay.absenceType]} />
              ) : (
                j
              )}
            </TeamCalendarDay>
          </TeamCalendarUserTd>
        );
      }
      days.push(
        <TeamCalendarUserTr key={'TeamCalendarUserTr' + user.id}>
          {line}
        </TeamCalendarUserTr>
      );
    });

    return days;
  }

  renderTotals = () => {
    var totals = [];


    this.props.users.forEach(user => {
      var total_deducted = 0;
      var total_non_deducted = 0;
      var total_sick = 0;
      var total_bonus = 0;

      user.days.forEach(day => {
          if ( day.requestStatus === 'APPROVED' ){
            switch (day.absenceType)
            {
              case 'SICKNESS' :
                total_sick++;
                break;
              case 'DEDUCTED_LEAVE' :
                total_deducted++;
                break;
              case 'NON_DEDUCTED_LEAVE' :
                total_non_deducted++;
                break;
              case 'BONUS_DAYS' :
                total_bonus++;
                break;
            }
          }


      })

      totals.push(
        <tr key={'TotalsTr' + user.id}>
          <TeamCalendarTotalsTd key={'TeamCalendarTotalsTd1' + user.id}>
            {total_deducted}
          </TeamCalendarTotalsTd>
          <TeamCalendarTotalsTd key={'TeamCalendarTotalsTd2' + user.id}>
            {total_non_deducted}
          </TeamCalendarTotalsTd>
          <TeamCalendarTotalsTd key={'TeamCalendarTotalsTd3' + user.id}>
            {total_sick}
          </TeamCalendarTotalsTd>
          <TeamCalendarTotalsTd key={'TeamCalendarTotalsTd4' + user.id}>
            {total_bonus}
          </TeamCalendarTotalsTd>
        </tr>
      );
    });
    return totals;
  }
  renderGridCalendarUsers = (year,month,d) =>
  {
    var users = []
    var cnt = 0;
    this.props.users.forEach( user => {
      user.days.forEach(day => {
          cnt++;
          if (cnt <= 5 && moment(new Date(year,month-1 , d)).format("YYYY-MM-DD") === day.date){
            users.push(
                <div key={'GridCalendarDayUser'+day.date+'-'+user.id}>
                  <GridLeaveImage requestStatus={day.requestStatus}>
                    <img src={leaveTypes[day.absenceType]} />
                  </GridLeaveImage>
                  {user.fullName}
                </div>)
          }
      })

    });
      return users;
  }
  renderGridCalendar = () => {
    var grid = [];
    for (var j = 1 - new Date(this.state.currentYear,this.state.currentMonth - 1,1).getDay(); j <= 35 - new Date().getDay(); ) {
      var row = [];
      for (var i = 0; i < 7; i++) {
        j++;
        row.push(
          <TeamCalendarGridTd key={'TeamCalendarGridTd' + i}>
            <div>{new Date(this.state.currentYear,this.state.currentMonth - 1,j).getDate()}</div>
            {this.renderGridCalendarUsers(
                  this.state.currentYear,
                  this.state.currentMonth,
                  j)}
          </TeamCalendarGridTd>
        );
      }
      grid.push(<tr key={'TeamCalendarGridTr' + j}>{row}</tr>);
    }
    return grid;
  }

  prevMonth = () => {
    if (--this.state.currentMonth < 1){
      this.state.currentMonth = 12;
      this.state.currentYear--;
    }

    this.forceUpdate();
  }

  nextMonth = () => {
    if (++this.state.currentMonth > 12){
      this.state.currentMonth = 1;
      this.state.currentYear++;
    }

    this.forceUpdate();
  }

  render = () => {

    var monthsOptions = [];
    var y = 2019;
    var m = 1;

    while ( y < moment().year() + 2 )
    {
      monthsOptions.push(
          <option key={'monthOption'+y+m} value={y*100 + m} >
            {moment(new Date(y,m - 1,1)).format('MMMM')} {y}
          </option>
      );
      if (++m > 12)
      {
        m=1;
        y++;
      }
    }

    return (
      <TeamCalendarWrapper>
        <TeamCalendarMenus>
          <select>
            <option>All Users</option>
            <option>My Team</option>
          </select>

          <TeamCalendarViewButton onClick={() => {this.state.viewMode = 'LIST'; this.forceUpdate()}}> List View </TeamCalendarViewButton>
          <TeamCalendarViewButton onClick={() => {this.state.viewMode = 'GRID';this.forceUpdate();}}> Calendar View </TeamCalendarViewButton>

          <TeamCalendarNavButton onClick={ this.prevMonth }> {'<'} </TeamCalendarNavButton>
          <TeamCalendarNavSelect defaultValue={this.state.currentYear*100 + this.state.currentMonth}>
                {monthsOptions}
          </TeamCalendarNavSelect>
          <TeamCalendarNavButton onClick={ this.nextMonth }> {'>'} </TeamCalendarNavButton>
        </TeamCalendarMenus>

        { this.state.viewMode === 'LIST' ?

          <TeamCalendarContainer>
            <TeamUsersContainer>{this.renderTeamUsers()}</TeamUsersContainer>
              <TeamDaysTable>
                <tbody>
                  <tr>{this.renderDaysHeader()}</tr>
                  {this.renderDays()}
                </tbody>
              </TeamDaysTable>
              <TeamTotalsTable>
                <tbody>
                 <tr>
                  <TeamCalendarTotalsTh> Deducted Days </TeamCalendarTotalsTh>
                  <TeamCalendarTotalsTh> Non Deducted Days </TeamCalendarTotalsTh>
                  <TeamCalendarTotalsTh> Sick Days </TeamCalendarTotalsTh>
                  <TeamCalendarTotalsTh> Bonus Days </TeamCalendarTotalsTh>
                 </tr>
                  {this.renderTotals()}
                </tbody>
               </TeamTotalsTable>
            </TeamCalendarContainer>

        :
        <TeamCalendarGridContainer>
          <table>
            <tbody>
              <tr>
                <th>Monday</th>
                <th>Tuesday</th>
                <th>Wednesday</th>
                <th>Thursday</th>
                <th>Friday</th>
                <th>Saturday</th>
                <th>Sunday</th>
              </tr>
              {this.renderGridCalendar()}
            </tbody>
          </table>
        </TeamCalendarGridContainer>
        }
      </TeamCalendarWrapper>


    );
  }
}

const mapStateToProps = state => ({
  users: getUsers(state),
  publicHolidays: getPublicHolidays(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestUsers,
      requestPublicHolidays
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TeamCalendar);
