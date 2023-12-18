import {Header} from "../header/Header.tsx";
import {Footer} from "./Footer.tsx";
import {useContext} from "react";
import {WcagContext} from "./WcagContextProvider.tsx";

export const DeclarationOfAvailability = () => {
    const {fontSize} = useContext(WcagContext)

    return (
        <>
            <Header/>
            <div className={`${fontSize}`} style={{"overflow": "auto", "textAlign": "left"}}>
                <h1 className={`h1-${fontSize}`} id="a11y-declaration">Accessibility Declaration Federated EGA Poland</h1>
                <p id="a11y-introduction"><span id="a11y-entity">University of Łódź</span> undertakes to ensure the
                    accessibility of its website in accordance with the provisions of the Act of April 4, 2019, on
                    digital accessibility of websites and mobile applications of public entities. This accessibility
                    statement applies to the website <a id="a11y-url" href="https://www.test-fega.uni.lodz.pl">Federated
                        FEGA Poland</a>.</p>
                <ul>
                    <li>Website publication date: <time id="a11y-publication-date" dateTime="2023-12-18">December 18,
                        2023</time></li>
                    <li>Date of the last significant update: <time id="a11y-last-update-date"
                                                                   dateTime="2023-12-18">December 18, 2023</time></li>
                </ul>
                <h2>Compliance Status with the Law</h2>
                <p>The website is <strong id="a11y-status">compliant</strong> with the Act on the digital accessibility
                    of websites and mobile applications of public entities.</p>
                <h3>Preparation of the Accessibility Declaration</h3>
                <ul>
                    <li>Declaration prepared on: <time id="a11y-preparation-date" dateTime="2023-12-18">December 18,
                        2023</time></li>
                    <li>Last review and update of the declaration on: <time id="a11y-declaration-review-date"
                                                                            dateTime="2023-12-18">December 18,
                        2023</time></li>
                </ul>

                <p>The declaration was prepared based on an assessment by an external entity conducted by <span
                    id="a11y-auditor">Smart Accessibility Technologies sp. z o.o.</span>.</p>

                <p>Download the <a href="https://sat-govtech.pl/wp-content/uploads/2023/12/Reaudyt-WCAG-Lodzpdf.pdf">Accessibility
                    Review Report</a>.</p>

                <h2 className={`h2-${fontSize}`} id="a11y-contact">Feedback and Contact Information</h2>
                <ul>
                    <li>Responsible for handling comments and suggestions: <span
                        id="a11y-person">Błażej Marciniak.</span>
                    </li>
                    <li>Email: <span id="a11y-email">blazej.marciniak@biol.uni.lodz.pl</span></li>
                    <li>Phone: <span id="a11y-phone">+48426356530</span></li>
                </ul>

                <p id="a11y-procedure">Everyone has the right to:</p>
                <ul>
                    <li>report comments on the digital accessibility of the website or its element,</li>
                    <li>submit a request for the provision of digital accessibility to the website or its element,</li>
                    <li>request the provision of unavailable information in an alternative form.</li>
                </ul>
                <p>The request must include:</p>
                <ul>
                    <li>contact details of the person reporting,</li>
                    <li>indication of the website or element of the website to which the request relates,</li>
                    <li>indication of a convenient form of providing information, if the request concerns providing
                        alternative information that is not accessible.
                    </li>
                </ul>
                <p>The review of the report should take place immediately, no later than 7 days. If within this period,
                    ensuring accessibility or providing access in an alternative form is not possible, it should occur
                    no
                    later than 2 months from the date of the report.</p>

                <h3 className={`h3-${fontSize}`}>Complaints and Appeals</h3>
                <p>Complaints about failure to meet these deadlines and refusal to fulfill a request can be submitted to
                    the
                    supervisory authority by mail or electronically to the address:</p>
                <ul>
                    <li>Supervisory authority: University of Łódź</li>
                    <li>Address: ul. Narutowicza 68, 90-136 Łódź</li>
                    <li>Email: natalia.halicka@uni.lodz.pl</li>
                    <li>Phone: +48 42 635 48 28</li>
                </ul>
                <p>A complaint can also be submitted to the <a
                    href="https://www.rpo.gov.pl/content/jak-zglosic-sie-do-rzecznika-praw-obywatelskich">Ombudsman for
                    Citizen's Rights</a>.</p>

                <Footer/>
            </div>
        </>
    )
}