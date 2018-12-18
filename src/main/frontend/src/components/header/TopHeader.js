import React, { Component, Fragment } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import UserInfo from '../UserInfo';
import { getUser } from '../../store/selectors';
import { requestUser } from '../../store/actions/userActions';
import ExecomLogo from '../../img/execom_logo.png';
import styled from 'styled-components';

const NavHeader = styled.header`
  width: 100%;
  height: 50px;
  background-color: #ededed;
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  justify-content: space-between;
`;

const LogoImage = styled.img`
  height: 30px;
`;

const NavSpan = styled.span`
  width: 100px;
`;
class TopHeader extends Component {
  componentDidMount() {
    this.props.requestUser(sessionStorage.getItem('userEmail'));
  }
  render() {
    return (
      <Fragment>
        <NavHeader>
          <NavSpan />
          <LogoImage src={ExecomLogo} />
          <UserInfo
            fullName={this.props.user.fullName}
            userEmail={this.props.user.email}
          />
        </NavHeader>
      </Fragment>
    );
  }
}

const mapStateToProps = state => ({
  user: getUser(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestUser
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TopHeader);
