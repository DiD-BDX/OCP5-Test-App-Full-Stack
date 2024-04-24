/// <reference types="cypress" />
describe('not found', () => {
    it('should display not found page', () => {
        cy.visit('/not-found')
        cy.contains('Page not found !')
        cy.url().should('include', '/404')
    })
})