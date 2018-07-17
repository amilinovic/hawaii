import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { requestApiData } from '../../store/actions/EmployeesActions';
import UserInfo from '../UserInfo';
import { getEmployee, getFetching } from '../../store/Selectors';

class TopHeader extends Component {
  componentDidMount() {
    this.props.requestApiData();
  }

  render() {
    return (
      // TODO change mocked data with actual data
      <Container fluid>
        <Row className="justify-content-between align-items-end py-2">
          <Col xs="3">
            <img
              src="https://lh3.googleusercontent.com/Zeu3x2hWQxFtX6Mh8DYIgdqkkzN8EXI5C5regdIJm-lf0mCe2hKkQrs-kzSN7bTlY5IJbKuRI_N1Y5VLx6Z_0V51Ndk8fJ5oCIgmFg=h43"
              alt="hawaii logo"
            />
          </Col>
          <Col xs="3" className="text-center d-none d-lg-block">
            <img
              src="https://lh3.googleusercontent.com/Zeu3x2hWQxFtX6Mh8DYIgdqkkzN8EXI5C5regdIJm-lf0mCe2hKkQrs-kzSN7bTlY5IJbKuRI_N1Y5VLx6Z_0V51Ndk8fJ5oCIgmFg=h43"
              alt="execom logo"
            />
          </Col>
          <Col xs="3" className="justify-content-end d-none d-lg-inline-flex">
            {this.props.fetching === '' ? null : (
              <UserInfo employeeInfo={this.props.employee.results[0]} />
            )}
          </Col>
        </Row>
      </Container>
    );
  }
}

const mapStateToProps = state => ({
  employee: getEmployee(state),
  fetching: getFetching(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestApiData
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TopHeader);
