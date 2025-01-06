import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { TechCVDocsService } from '../service/tech-cv-docs.service';
import { ITechCVDocs } from '../tech-cv-docs.model';
import { TechCVDocsFormService, TechCVDocsFormGroup } from './tech-cv-docs-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-docs-update',
  templateUrl: './tech-cv-docs-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechCVDocsUpdateComponent implements OnInit {
  isSaving = false;
  techCVDocs: ITechCVDocs | null = null;

  technicalCVSSharedCollection: ITechnicalCV[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected techCVDocsService = inject(TechCVDocsService);
  protected techCVDocsFormService = inject(TechCVDocsFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechCVDocsFormGroup = this.techCVDocsFormService.createTechCVDocsFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techCVDocs }) => {
      this.techCVDocs = techCVDocs;
      if (techCVDocs) {
        this.updateForm(techCVDocs);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('hrSolutionApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techCVDocs = this.techCVDocsFormService.getTechCVDocs(this.editForm);
    if (techCVDocs.id !== null) {
      this.subscribeToSaveResponse(this.techCVDocsService.update(techCVDocs));
    } else {
      this.subscribeToSaveResponse(this.techCVDocsService.create(techCVDocs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechCVDocs>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(techCVDocs: ITechCVDocs): void {
    this.techCVDocs = techCVDocs;
    this.techCVDocsFormService.resetForm(this.editForm, techCVDocs);

    this.technicalCVSSharedCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.technicalCVSSharedCollection,
      techCVDocs.technicalCV,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query()
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.techCVDocs?.technicalCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.technicalCVSSharedCollection = technicalCVS));
  }
}
