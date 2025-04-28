**TermService Operationen**

<table>
<colgroup>
<col style="width: 32%" />
<col style="width: 7%" />
<col style="width: 20%" />
<col style="width: 21%" />
<col style="width: 7%" />
<col style="width: 8%" />
</colgroup>
<thead>
<tr class="header">
<th>Signatur</th>
<th>Asyn/Sync</th>
<th>Grund</th>
<th>Mögliche Errors</th>
<th>Thin / Fat</th>
<th>CRUD / Dedicated</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td>Term createTerm(String eventId, TermData termData)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td>Event nicht gefunden, TermData ist nicht valide</td>
<td>Thin</td>
<td>CRUD</td>
</tr>
<tr class="even">
<td>Term getTerm(String termId)</td>
<td>Sync</td>
<td>Direkte Abfrage für Information</td>
<td>Term nicht gefunden</td>
<td></td>
<td></td>
</tr>
<tr class="odd">
<td>Term updateTerm(String termId, TermData termData)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td>Term nicht gefunden, TermData ist nicht valide</td>
<td>Thin</td>
<td>CRUD</td>
</tr>
<tr class="even">
<td>Boolean deleteTerm(String termId)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td><p>Term nicht gefunden/löschbar</p>
<p>Offene Payments existieren</p></td>
<td>Thin</td>
<td>CRUD</td>
</tr>
<tr class="odd">
<td>Term[] getTermsWithoutCareGiver(eventId)</td>
<td>Sync</td>
<td>Direkte Abfrage für Information</td>
<td>Event nicht gefunden</td>
<td></td>
<td></td>
</tr>
<tr class="even">
<td>Boolean assignCaregiver(String termId, string caregiverId)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td>Term/Caregiver nicht gefunden, Caregiver ist bereits zugeordnet</td>
<td>Fat</td>
<td>Dedicated</td>
</tr>
<tr class="odd">
<td>Boolean unAssignCaregiver(String termId)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td>Term nicht gefunden</td>
<td>Fat</td>
<td>Dedicated</td>
</tr>
</tbody>
</table>

**AccountingService Operationen**

<table>
<colgroup>
<col style="width: 32%" />
<col style="width: 8%" />
<col style="width: 19%" />
<col style="width: 22%" />
<col style="width: 7%" />
<col style="width: 9%" />
</colgroup>
<thead>
<tr class="header">
<th>Signatur</th>
<th>Async/Sync</th>
<th>Grund</th>
<th>Mögliche Errors</th>
<th>Thin / Fat</th>
<th>CRUD / Dedicated</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td>Payment[] getPayments(String termId)</td>
<td>Sync</td>
<td>Direkte Abfrage für Information</td>
<td>Term nicht gefunden</td>
<td></td>
<td></td>
</tr>
<tr class="even">
<td>Payment[] getOpenPayments(String termId)</td>
<td>Sync</td>
<td>Direkte Abfrage</td>
<td>Term nicht gefunden</td>
<td></td>
<td></td>
</tr>
<tr class="odd">
<td>Payment[] getOpenPayments(String userId)</td>
<td>Sync</td>
<td>Direkte Abfrage</td>
<td>User nicht gefunden</td>
<td></td>
<td></td>
</tr>
<tr class="even">
<td>Payment createUserPayment(String termId, String userId, PaymentData
paymentData)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td><p>Term/User nicht gefunden, Payment existiert bereits,</p>
<p>PaymentData nicht valide</p></td>
<td>Thin</td>
<td>CRUD</td>
</tr>
<tr class="odd">
<td>Void notifyUserPaymentReceived(String userId, String termId)</td>
<td>Async</td>
<td>Kein sofortiges Feedback erforderlich</td>
<td>User/Term nicht gefunden</td>
<td>Fat</td>
<td>Dedicated</td>
</tr>
<tr class="even">
<td>Void notifyUserPaymentPending(String userId, String termId, double
price, LocalDate paymentDeadline)</td>
<td>Async</td>
<td>Kein sofortiges Feedback erforderlich</td>
<td>User/Term nicht gefunden</td>
<td>Fat</td>
<td>Dedicated</td>
</tr>
<tr class="odd">
<td>PaymentStatus getUserPaymentStatusForTerm(String userId, String
termId)</td>
<td>Sync</td>
<td>Direkte Abfrage</td>
<td>User/Term nicht gefunden</td>
<td></td>
<td></td>
</tr>
<tr class="even">
<td>Boolean updatePaymentStatus(String termId, String userId,
PaymentStatus status)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td>Term/User nicht gefunden, Status nicht valide</td>
<td>Thin</td>
<td>CRUD</td>
</tr>
<tr class="odd">
<td>Boolean deletePayment(String paymentId)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td>Payment nicht gefunden/löschbar</td>
<td>Thin</td>
<td>CRUD</td>
</tr>
<tr class="even">
<td>Payment updatePayment(String paymentId, PaymentData
paymentData)</td>
<td>Sync</td>
<td>Sofortiges Feedback benötigt</td>
<td>Payment nicht gefunden, PaymentData nicht valide</td>
<td>Thin</td>
<td>CRUD</td>
</tr>
</tbody>
</table>

**Note:** Async errors should be logged or retried since client is not
waiting.

**Kollaboratoren**

| Service       | Methode                                      | Async/Sync | Grund                             |
|---------------|----------------------------------------------|------------|-----------------------------------|
| MemberService | isCareGiverAvailable(string caregiverId)     | Sync       | Sofortiges Feedback notwendig     |
|               | notifyCareGiver(string caregiverId)          | Async      | Kein blockieren notwendig         |
| UserService   | isUserAvailable(String userId)               | Sync       | Sofortiges Feedback notwendig     |
|               | notifyUser(String userId)                    | Async      | Kein blockieren notwendig         |
| EventService  | getEventDetails(String eventId) /getEvents() | Sync       | Term Erstellung benötigt Event ID |

**  
**

<table>
<colgroup>
<col style="width: 20%" />
<col style="width: 79%" />
</colgroup>
<thead>
<tr class="header">
<th><strong>TermService</strong></th>
<th></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td>TermData Objekt</td>
<td><p>public class Term {</p>
<p>private String termId;</p>
<p>private String eventId;</p>
<p>private LocalDate date;</p>
<p>private LocalTime time;</p>
<p>private String location;</p>
<p>private String meetingPoint;</p>
<p>private int minParticipants;</p>
<p>private int maxParticipants;</p>
<p>private double price;</p>
<p>private List&lt;String&gt; caregivers;</p>
<p>}</p></td>
</tr>
<tr class="even">
<td>Methoden Signaturen</td>
<td><p>public Term createTerm(String eventId, Term termData);</p>
<p>public Term getTerm(String termId);</p>
<p>public Term updateTerm(String termId, Term termData);</p>
<p>public boolean deleteTerm(String termId);</p>
<p>public List&lt;Term&gt; getEventTermsWithoutCareGiver();</p>
<p>public boolean assignCareGiver(String termId, String
careGiverId);</p>
<p>public boolean unAssignCareGiver(String termId);</p>
<p>public boolean isCareGiverAssigned(String termId);</p></td>
</tr>
</tbody>
</table>

<table>
<colgroup>
<col style="width: 20%" />
<col style="width: 79%" />
</colgroup>
<thead>
<tr class="header">
<th><strong>AccountingService</strong></th>
<th></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td>PaymentData Objekt</td>
<td><p>public class Payment {</p>
<p>private String paymentId;</p>
<p>private String termId;</p>
<p>private String userId;</p>
<p>private double amount;</p>
<p>private PaymentStatus status;</p>
<p>private LocalDate paymentDate;</p>
<p>private LocalDate paymentDeadline;</p>
<p>}</p></td>
</tr>
<tr class="even">
<td>Methoden Signaturen</td>
<td><p>public List&lt;Payment&gt; getPayments(String termId);</p>
<p>public List&lt;Payment&gt; getOpenPayments(String termId);</p>
<p>public List&lt;Payment&gt; getOpenPayments(String userId);</p>
<p>public Payment createUser Payment(String termId, String userId);</p>
<p>public void notifyUser PaymentReceived(String userId, String
termId);</p>
<p>public void notifyUser PaymentPending(String userId, String termId,
double price, String paymentDeadline);</p>
<p>public PaymentStatus getUser PaymentStatusForTerm(String userId,
String termId);</p>
<p>public boolean updatePaymentStatus(String termId, String userId,
PaymentStatus status);</p></td>
</tr>
</tbody>
</table>

**Fragen:**

- Sollen die verschiedenen Benachrichtigungen (z.B. Payment Received,
  Payment Reminder etc.) verschiedene notify Methoden haben oder ist
  notifyUser ausreichend?

- TermData: Max und Min Teilnehmer, gehört das zu Event oder zum Term?
