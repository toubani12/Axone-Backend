import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDomain } from '../domain.model';
import { DomainService } from '../service/domain.service';

@Component({
  standalone: true,
  templateUrl: './domain-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DomainDeleteDialogComponent {
  domain?: IDomain;

  protected domainService = inject(DomainService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.domainService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
