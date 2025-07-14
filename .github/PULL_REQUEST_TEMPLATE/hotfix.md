## 🚨 HOTFIX SUMMARY
**URGENT:** Brief description of the critical issue and fix.

**Severity:** Critical/High
**Target Branch:** `main/production`

## Type of Change
- [x] Hotfix (urgent fix for production issue)
- [ ] Breaking change

## Related Issues
- Fixes #
- Production incident: [link to incident]

## 🔥 Critical Issue Details
### Impact Assessment
- **Downtime:** [duration or "none"]
- **Affected users:** [percentage or "all users"]
- **Business impact:** [revenue, user experience, etc.]
- **Services affected:** [list services]

### Root Cause
**What broke?**
[Detailed explanation]

**When did it break?**
[Timeline]

**Why did it break?**
[Root cause analysis]

## 🛠️ Fix Details
### Changes Made
- [ ] 
- [ ] 
- [ ] 

### Fix Strategy
- **Approach:** [quick fix vs proper fix]
- **Risk level:** Low/Medium/High
- **Rollback plan:** [how to rollback if needed]

## ⚡ Testing (Expedited)
- [x] Critical path tested
- [x] Manual testing completed
- [x] Production-like environment tested
- [ ] Full regression testing (post-deployment)

### Verification Steps
1.
2.
3.
4. ✅ Issue resolved

### Quick Smoke Tests
- [ ] Core functionality works
- [ ] Authentication works
- [ ] Database connections stable
- [ ] API endpoints responding

## 📊 Monitoring
### Pre-Deployment Checks
- [ ] Monitoring dashboard reviewed
- [ ] Error rates checked
- [ ] Performance metrics stable

### Post-Deployment Monitoring
- [ ] Error rates monitoring
- [ ] Performance metrics tracking
- [ ] User impact assessment
- [ ] Rollback triggers identified

## 🚀 Deployment Plan
### Deployment Strategy
- **Method:** [blue-green, rolling, immediate]
- **Timing:** [ASAP/scheduled]
- **Rollback time:** [how quickly can we rollback]

### Deployment Checklist
- [ ] Database changes (if any) are backward compatible
- [ ] No additional environment variables needed
- [ ] Deployment scripts tested
- [ ] Rollback procedure documented

## 📋 Post-Deployment Tasks
- [ ] Monitor for 30 minutes post-deployment
- [ ] Verify metrics return to normal
- [ ] Update incident status
- [ ] Schedule proper fix (if this is a band-aid)
- [ ] Post-mortem scheduled

## 🔄 Follow-up Actions
- [ ] Create follow-up ticket for proper fix
- [ ] Update monitoring/alerting
- [ ] Review and improve prevention measures
- [ ] Document lessons learned

## Security Impact
- [x] No security implications
- [ ] Security review completed (expedited)
- [ ] Security vulnerability patched

## Communication
- [ ] Stakeholders notified
- [ ] Status page updated
- [ ] Customer support informed
- [ ] Team leads notified

## Reviewer Focus Areas
- [ ] **PRIORITY:** Does this fix the critical issue?
- [ ] Risk assessment of the fix
- [ ] Potential for new issues
- [ ] Rollback feasibility
- [ ] Monitoring adequacy

## Emergency Approval
**Required approvals:** [list required approvers]
- [ ] Technical lead approval
- [ ] Security approval (if needed)
- [ ] Product owner approval (if needed)

---
⚠️ **Note:** This is an emergency hotfix. Full testing and proper solution should follow in a separate PR.