import { render, screen } from '@testing-library/react';
import Intro from './Intro';

test('renders learn react link', () => {
  render(<Intro />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
