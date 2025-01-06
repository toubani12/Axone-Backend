import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { ITechCVProject } from '../tech-cv-project.model';
import { TechCVProjectService } from '../service/tech-cv-project.service';
import { TechCVProjectFormService, TechCVProjectFormGroup } from './tech-cv-project-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-project-update',
  templateUrl: './tech-cv-project-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechCVProjectUpdateComponent implements OnInit {
  isSaving = false;
  techCVProject: ITechCVProject | null = null;

  technicalCVSSharedCollection: ITechnicalCV[] = [];

  protected techCVProjectService = inject(TechCVProjectService);
  protected techCVProjectFormService = inject(TechCVProjectFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechCVProjectFormGroup = this.techCVProjectFormService.createTechCVProjectFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techCVProject }) => {
      this.techCVProject = techCVProject;
      if (techCVProject) {
        this.updateForm(techCVProject);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techCVProject = this.techCVProjectFormService.getTechCVProject(this.editForm);
    if (techCVProject.id !== null) {
      this.subscribeToSaveResponse(this.techCVProjectService.update(techCVProject));
    } else {
      this.subscribeToSaveResponse(this.techCVProjectService.create(techCVProject));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechCVProject>>): void {
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

  protected updateForm(techCVProject: ITechCVProject): void {
    this.techCVProject = techCVProject;
    this.techCVProjectFormService.resetForm(this.editForm, techCVProject);

    this.technicalCVSSharedCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.technicalCVSSharedCollection,
      techCVProject.technicalCV,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query()
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.techCVProject?.technicalCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.technicalCVSSharedCollection = technicalCVS));
  }
}
