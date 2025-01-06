import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechCVDocs } from '../tech-cv-docs.model';
import { TechCVDocsService } from '../service/tech-cv-docs.service';

@Component({
  standalone: true,
  templateUrl: './tech-cv-docs-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechCVDocsDeleteDialogComponent {
  techCVDocs?: ITechCVDocs;

  protected techCVDocsService = inject(TechCVDocsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techCVDocsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
