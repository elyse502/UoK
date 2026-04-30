function Content() {
  return (
    <div class="dashcontainer">
      <div class="dashgroup">
        <h2>Total Doctors</h2>
        <p id="paragraph">10</p>
        <a href="viewdoctors.php">Check</a>
      </div>

      <div class="dashgroup">
        <h2>Add Doctors</h2>
        <p class="paragraph">Doctors Data Entry</p>
        <a href="adddoctor.php">Add</a>
      </div>

      <div class="dashgroup">
        <h2>Update Doctors</h2>
        <p>Click to Change</p>
        <a href="updatedoctor.php?id=1">Update</a>
      </div>
    </div>
  );
}

export default Content;
