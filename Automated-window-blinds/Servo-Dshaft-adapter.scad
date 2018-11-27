union() {
difference() {
  cylinder(15, d=10, true);
  translate([0,0,5])
    cylinder(12, d=4.5, true);
  translate([0,0,-1])
    cylinder(4, d=4.8, true);
  cylinder(15,d=1,true);
}
  translate([2,0,10])
    cube([1,3,10], true);
}