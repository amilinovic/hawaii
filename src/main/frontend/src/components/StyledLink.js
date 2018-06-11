import styled from 'styled-components';

export const StyledLink = styled.span`
  color: ${props => (props.color ? props.color : 'white')};
  padding: 10px;
  border-radius: 5px;
  background: ${props => (props.background ? props.background : '#FA4C50')};
  border: 1px solid #3c3c46;
  display: ${props => (props.display ? props.display : 'inline-block')};
  &:before {
    content: '';
    background-image: url(${props => (props.icon ? props.icon : '')});
    width: ${props => (props.icon ? '20px' : '')};
    height: ${props => (props.icon ? '20px' : '')};
    float: left;
    background-size: cover;
    margin-right: ${props => (props.icon ? '10px' : '')};
  }
`;
